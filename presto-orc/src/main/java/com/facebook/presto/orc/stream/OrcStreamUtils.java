/*
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
package com.facebook.presto.orc.stream;

import com.facebook.presto.orc.OrcCorruptionException;

import java.io.IOException;
import java.io.InputStream;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

final class OrcStreamUtils
{
    public static final int MIN_REPEAT_SIZE = 3;

    private OrcStreamUtils()
    {
    }

    public static void skipFully(InputStream input, long length)
            throws IOException
    {
        while (length > 0) {
            long result = input.skip(length);
            if (result < 0) {
                throw new OrcCorruptionException("Unexpected end of stream");
            }
            length -= result;
        }
    }

    public static void readFully(InputStream input, byte[] buffer, int offset, int length)
            throws IOException
    {
        while (offset < length) {
            int result = input.read(buffer, offset, length - offset);
            if (result < 0) {
                throw new OrcCorruptionException("Unexpected end of stream");
            }
            offset += result;
        }
    }

    static <A, B extends A> B checkType(A value, Class<B> target, String name)
    {
        checkNotNull(value, "%s is null", name);
        checkArgument(target.isInstance(value),
                "%s must be of type %s, not %s",
                name,
                target.getName(),
                value.getClass().getName());
        return target.cast(value);
    }
}
