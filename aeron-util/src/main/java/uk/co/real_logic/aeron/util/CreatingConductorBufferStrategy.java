/*
 * Copyright 2014 Real Logic Ltd.
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
package uk.co.real_logic.aeron.util;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;

import static uk.co.real_logic.aeron.util.IoUtil.mapNewFile;

public class CreatingConductorBufferStrategy extends ConductorBufferStrategy
{

    private final File adminDir;
    private final int bufferSize;

    private MappedByteBuffer toMediaDriverBuffer = null;
    private MappedByteBuffer toApiBuffer = null;

    public CreatingConductorBufferStrategy(final String adminDir, final int bufferSize)
    {
        super(adminDir);
        this.adminDir = new File(adminDir);
        this.bufferSize = bufferSize;
    }

    public ByteBuffer toMediaDriver() throws IOException
    {
        if (toMediaDriverBuffer == null)
        {
            IoUtil.checkDirectoryExists(adminDir, "adminDir");
            toMediaDriverBuffer = mapNewFile(toMediaDriver, MEDIA_DRIVER_FILE, bufferSize);
        }
        return toMediaDriverBuffer;
    }

    public ByteBuffer toApi() throws IOException
    {
        if (toApiBuffer == null)
        {
            IoUtil.checkDirectoryExists(adminDir, "adminDir");
            toApiBuffer = mapNewFile(toApi, API_FILE, bufferSize);
        }
        return toApiBuffer;
    }

    public void close()
    {
        IoUtil.unmap(toApiBuffer);
        IoUtil.unmap(toMediaDriverBuffer);
        toApiBuffer = null;
        toMediaDriverBuffer = null;
    }
}
