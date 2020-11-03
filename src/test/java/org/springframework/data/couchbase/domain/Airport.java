/*
 * Copyright 2017-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.data.couchbase.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.couchbase.core.mapping.Document;

/**
 * Airport entity
 *
 * @author Michael Nitschinger
 * @author Michael Reiche
 */
@Document
public class Airport {
	@Id String id;

	String iata;

	String icao;

	String _class;

	public Airport(String id, String iata, String icao) {
		this.id = id;
		this.iata = iata;
		this.icao = icao;
	}

	public Airport() {
	}

	public String getId() {
		return id;
	}

	public void setId(String id) { this.id = id; }

	public String getIata() {
		return iata;
	}
  public void setIata(String iata) { this.iata = iata; }

	public String getIcao() {
		return icao;
	}
	public void setIcao(String icao) { this.icao = icao; }

	public String get_class(){ return _class;}
	public void set_class(String _class) { this._class = _class; }

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("{ id: ");
		sb.append(getId());
		sb.append(", iata: ");
		sb.append(iata);
		sb.append(", icao: ");
		sb.append(icao);
		sb.append(" }");
		return sb.toString();
	}
}
