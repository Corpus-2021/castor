/*
 * Copyright 2005 Philipp Erlacher
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.exolab.castor.xml;

/**
 * This class is part of the command pattern implementation to instantiate an
 * object. It is used as a command by the command invoker PrimitiveObject.
 * 
 * @author <a href="mailto:philipp DOT erlacher AT gmail DOT com">Philipp
 *         Erlacher</a>
 * 
 */
public class PrimitiveBoolean extends PrimitiveObject {

    @Override
    public Object getObject() {
        if (isNull()) {
            return Boolean.FALSE;
        }

        if (value.equals("1") || value.toLowerCase().equals("true")) {
            return Boolean.TRUE;
        }

        if (value.equals("0") || value.toLowerCase().equals("false")) {
            return Boolean.FALSE;
        }

        throw new IllegalArgumentException(" A value of >" + value
                + "< cannot be converted to a boolean value.");
    }

}
