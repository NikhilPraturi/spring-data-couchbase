package org.springframework.data.couchbase.repository.query;

import org.reactivestreams.Publisher;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.couchbase.core.CouchbaseOperations;
import org.springframework.data.couchbase.core.ReactiveCouchbaseOperations;
import org.springframework.data.couchbase.core.convert.CouchbaseConverter;
import org.springframework.data.mapping.model.EntityInstantiators;
import org.springframework.data.repository.query.ResultProcessor;
import org.springframework.data.repository.query.ReturnedType;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

final class ResultProcessingConverter<CouchbaseOperationsType> implements Converter<Object, Object> {

	private final ResultProcessor processor;
	private final CouchbaseOperationsType operations;
	private final EntityInstantiators instantiators;

	public ResultProcessingConverter(ResultProcessor processor, CouchbaseOperationsType operations,
			EntityInstantiators instantiators) {

		Assert.notNull(processor, "Processor must not be null!");
		Assert.notNull(operations, "Operations must not be null!");
		Assert.notNull(instantiators, "Instantiators must not be null!");

		this.processor = processor;
		this.operations = operations;
		this.instantiators = instantiators;
	}

	/*
	 * (non-Javadoc)
	 * @see org.springframework.core.convert.converter.Converter#convert(java.lang.Object)
	 */
	@Override
	public Object convert(Object source) {

		ReturnedType returnedType = processor.getReturnedType();

		if (isVoid(returnedType)) {

			if (source instanceof Mono) {
				return ((Mono<?>) source).then();
			}

			if (source instanceof Publisher) {
				return Flux.from((Publisher<?>) source).then();
			}
		}

		if (ClassUtils.isPrimitiveOrWrapper(returnedType.getReturnedType())) {
			return source;
		}

		CouchbaseConverter cvtr = operations instanceof CouchbaseOperations
				? ((CouchbaseOperations) operations).getConverter()
				: ((ReactiveCouchbaseOperations) operations).getConverter();
		if (!cvtr.getMappingContext().hasPersistentEntityFor(returnedType.getReturnedType())) {
			return source;
		}

		Converter<Object, Object> converter = new DtoInstantiatingConverter(returnedType.getReturnedType(),
				cvtr.getMappingContext(), instantiators);

		return processor.processResult(source, converter);
	}

	static boolean isVoid(ReturnedType returnedType) {
		return returnedType.getReturnedType().equals(Void.class);
	}
}
