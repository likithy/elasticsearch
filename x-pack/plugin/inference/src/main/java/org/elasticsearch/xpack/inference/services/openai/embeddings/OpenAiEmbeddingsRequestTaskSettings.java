/*
 * Copyright Elasticsearch B.V. and/or licensed to Elasticsearch B.V. under one
 * or more contributor license agreements. Licensed under the Elastic License
 * 2.0; you may not use this file except in compliance with the Elastic License
 * 2.0.
 */

package org.elasticsearch.xpack.inference.services.openai.embeddings;

import org.elasticsearch.common.ValidationException;
import org.elasticsearch.inference.ModelConfigurations;

import java.util.Map;

import static org.elasticsearch.xpack.inference.services.ServiceUtils.extractOptionalString;
import static org.elasticsearch.xpack.inference.services.openai.embeddings.OpenAiEmbeddingsTaskSettings.MODEL;
import static org.elasticsearch.xpack.inference.services.openai.embeddings.OpenAiEmbeddingsTaskSettings.USER;

/**
 * This class handles extracting OpenAI task settings from a request. The difference between this class and
 * {@link OpenAiEmbeddingsTaskSettings} is that this class considers all fields as optional. It will not throw an error if a field
 * is missing. This allows overriding persistent task settings.
 * @param model the name of the model to use with this request
 * @param user a unique identifier representing your end-user, which can help OpenAI to monitor and detect abuse
 */
public record OpenAiEmbeddingsRequestTaskSettings(String model, String user) {
    public static final OpenAiEmbeddingsRequestTaskSettings EMPTY_SETTINGS = new OpenAiEmbeddingsRequestTaskSettings(null, null);

    /**
     * Extracts the task settings from a map. All settings are considered optional and the absence of a setting
     * does not throw an error.
     * @param map the settings received from a request
     * @return a {@link OpenAiEmbeddingsRequestTaskSettings}
     */
    public static OpenAiEmbeddingsRequestTaskSettings fromMap(Map<String, Object> map) {
        if (map.isEmpty()) {
            return OpenAiEmbeddingsRequestTaskSettings.EMPTY_SETTINGS;
        }

        ValidationException validationException = new ValidationException();

        String model = extractOptionalString(map, MODEL, ModelConfigurations.TASK_SETTINGS, validationException);
        String user = extractOptionalString(map, USER, ModelConfigurations.TASK_SETTINGS, validationException);

        if (validationException.validationErrors().isEmpty() == false) {
            throw validationException;
        }

        return new OpenAiEmbeddingsRequestTaskSettings(model, user);
    }
}