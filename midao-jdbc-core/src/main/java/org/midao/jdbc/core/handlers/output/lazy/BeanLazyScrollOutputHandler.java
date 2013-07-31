/*
 * Copyright 2013 Zakhar Prykhoda
 *
 *    midao.org
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package org.midao.jdbc.core.handlers.output.lazy;

import org.midao.jdbc.core.MjdbcConfig;
import org.midao.jdbc.core.exception.MjdbcException;
import org.midao.jdbc.core.exception.MjdbcRuntimeException;
import org.midao.jdbc.core.handlers.model.QueryParameters;
import org.midao.jdbc.core.handlers.model.QueryParametersLazyList;
import org.midao.jdbc.core.processor.QueryOutputProcessor;

import java.util.List;

/**
 * Converts query output into scrollable lazy list of beans
 *
 * @see {@link LazyScrollOutputHandler}
 */
public class BeanLazyScrollOutputHandler<S> extends AbstractScrollUpdateLazyOutputHandler implements LazyScrollOutputHandler<BeanLazyScrollOutputHandler, S> {
    private final Class<S> type;

    /**
     * Creates new BeanLazyScrollOutputHandler instance.
     *
     * @param type Bean Class description
     */
    public BeanLazyScrollOutputHandler(Class<S> type) {
        this(type, MjdbcConfig.getDefaultQueryOutputProcessor());
    }

    /**
     * Creates new BeanLazyScrollOutputHandler instance.
     *
     * @param type Bean Class description
     * @param processor Query output processor
     */
    public BeanLazyScrollOutputHandler(Class<S> type, QueryOutputProcessor processor) {
        this.type = type;
        this.processor = processor;
    }

    /**
     * Creates new BeanLazyScrollOutputHandler instance.
     *
     * @param type Bean Class description
     * @param processor Query output processor
     * @param paramsList Query output lazy list
     */
    private BeanLazyScrollOutputHandler(Class<S> type, QueryOutputProcessor processor, QueryParametersLazyList paramsList) {
        this.type = type;
        this.processor = processor;
        this.queryParams = paramsList.getLazyCacheIterator();
    }

    /**
     * {@inheritDoc}
     */
    public boolean hasNext() {
        return super.innerHasNext();
    }

    /**
     * {@inheritDoc}
     */
    public S getNext() {
        S result = null;
        QueryParameters params = innerGetNext();

        try {
            result = processor.toBean(params, this.type);
        } catch (MjdbcException ex) {
            throw new MjdbcRuntimeException(ex);
        }

        return result;
    }

    /**
     * {@inheritDoc}
     */
    public S getCurrent() {
        S result = null;

        QueryParameters params = innerGetCurrent();

        try {
            result = processor.toBean(params, this.type);
        } catch (MjdbcException ex) {
            throw new MjdbcRuntimeException(ex);
        }

        return result;
    }

    /**
     * {@inheritDoc}
     */
    public void close() {
        innerClose();
    }

    /**
     * {@inheritDoc}
     */
    public BeanLazyScrollOutputHandler handle(List<QueryParameters> outputList) throws MjdbcException {
        if (outputList instanceof QueryParametersLazyList) {
            return new BeanLazyScrollOutputHandler(this.type, this.processor, (QueryParametersLazyList) outputList);
        } else {
            throw new MjdbcRuntimeException("LazyOutputHandler can be used only together with LazyStatementHandler. \n" +
                    "Please assign LazyStatementHandler to this QueryRunner or create new QueryRunnerService via MjdbcFactory");
        }
    }

    /**
     * {@inheritDoc}
     */
    public boolean hasPrev() {
        return innerHasPrev();
    }

    /**
     * {@inheritDoc}
     */
    public S getPrev() {
        S result = null;
        QueryParameters params = innerGetPrev();

        try {
            result = processor.toBean(params, this.type);
        } catch (MjdbcException ex) {
            throw new MjdbcRuntimeException(ex);
        }

        return result;
    }

    /**
     * {@inheritDoc}
     */
    public boolean moveTo(int row) {
        return innerMoveTo(row);
    }

    /**
     * {@inheritDoc}
     */
    public boolean moveRelative(int rows) {
        return innerMoveRelative(rows);
    }

    /**
     * {@inheritDoc}
     */
    public int position() {
        return innerPosition();
    }
}
