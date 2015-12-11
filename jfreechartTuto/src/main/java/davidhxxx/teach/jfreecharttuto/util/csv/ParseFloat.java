/*
 * Copyright 2007 Kasper B. Graversen
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package davidhxxx.teach.jfreecharttuto.util.csv;

import org.supercsv.cellprocessor.CellProcessorAdaptor;
import org.supercsv.cellprocessor.ift.StringCellProcessor;
import org.supercsv.exception.SuperCsvCellProcessorException;
import org.supercsv.util.CsvContext;

/**
 * Converts a String to a Float.
 * 
 */
public class ParseFloat extends CellProcessorAdaptor implements StringCellProcessor {

    /**
     * Constructs a new <tt>ParseFloat</tt> processor, which converts a String to a Float.
     */
    public ParseFloat() {
	super();
    }

    /**
     * Constructs a new <tt>ParseFloat</tt> processor, which converts a String to a Float, then calls the next processor in the chain.
     * 
     * @param next
     *            the next processor in the chain
     * @throws NullPointerException
     *             if next is null
     */
    public ParseFloat(final FloatCellProcessor next) {
	super(next);
    }

    /**
     * {@inheritDoc}
     * 
     * @throws SuperCsvCellProcessorException
     *             if value is null, isn't a Float or String, or can't be parsed as a Float
     */
    @Override
    public Object execute(Object value, final CsvContext context) {
	validateInputNotNull(value, context);

	final Float result;
	if (value instanceof Float) {
	    result = (Float) value;
	} else if (value instanceof String) {
	    try {
		value = value.toString().replaceAll(",", ".");
		result = new Float((String) value);
	    } catch (final NumberFormatException e) {
		throw new SuperCsvCellProcessorException(String.format("'%s' could not be parsed as a Float", value), context, this, e);
	    }
	} else {
	    final String actualClassName = value.getClass().getName();
	    throw new SuperCsvCellProcessorException(String.format("the input value should be of type Float or String but is of type %s", actualClassName),
		    context, this);
	}

	return next.execute(result, context);
    }
}
