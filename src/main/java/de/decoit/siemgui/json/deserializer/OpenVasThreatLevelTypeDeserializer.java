/* 
 * Copyright (C) 2015 DECOIT GmbH
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package de.decoit.siemgui.json.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import de.decoit.siemgui.domain.events.OpenVasThreatLevel;
import java.io.IOException;



/**
 *
 * @author Thomas Rix (rix@decoit.de)
 */
public class OpenVasThreatLevelTypeDeserializer extends JsonDeserializer<OpenVasThreatLevel> {
	@Override
	public OpenVasThreatLevel deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
		try {
			return OpenVasThreatLevel.fromThreatString(jp.getValueAsString());
		}
		catch(IllegalArgumentException ex) {
			throw new JsonMappingException("JSON deserialization failed with IllegalArgumentException", ex);
		}
	}
}
