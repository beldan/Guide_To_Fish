/*
 * Copyright (C) 2012 47 Degrees, LLC
 * http://47deg.com
 * hello@47deg.com
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

package beldan.restrung.misc;

import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;

import beldan.restrung.client.APIPostParams;

/**
 * An implementation of MultipartEntity entity that allows the caller to get upload progress
 */
public class CountingMultipartEntity extends MultipartEntity {

    /**
     * The listener receiving progress updates
     */
    private final APIPostParams listener;

    /**
     * The file's length
     */
    private long fileLength = 0;

    /**
     * Constructor that creates a multi part entity for a POST or PUT request
     * and accepts a listner for upload progress
     *
     * @param mode       @see HttpMultipartMode
     * @param boundary   the boundary to determine when a part ends and another one starts
     * @param charset    the default charset for encoding purposes
     * @param fileLength the known file length
     * @param listener   a listener to receive upload progress
     */
    public CountingMultipartEntity(HttpMultipartMode mode, final String boundary,
                                   final Charset charset, long fileLength, APIPostParams listener) {
        super(mode, boundary, charset);
        this.fileLength = fileLength;
        this.listener = listener;
    }

    /**
     * @see MultipartEntity#writeTo(java.io.OutputStream)
     */
    @Override
    public void writeTo(final OutputStream outstream) throws IOException {
        super.writeTo(new CountingOutputStream(outstream, this.listener, fileLength));
    }

}
