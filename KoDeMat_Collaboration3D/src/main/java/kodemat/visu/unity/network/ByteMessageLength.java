/* 
 * Copyright 2014 Institute fml (TU Munich) and Institute FLW (TU Dortmund).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package kodemat.visu.unity.network;

/**
 * Encodes a message length in a single bytes, thus the maximum message length is 255 bytes.
 */
public final class ByteMessageLength implements MessageLength {
	
	private final int NUM_BYTES = 1;
	private final long MAX_LENGTH = 255;
	
	/**
	 * @see com.cordinc.faraway.server.network.MessageLength#byteLength()
	 */
	@Override public int byteLength() {
		return NUM_BYTES;
	}
	
	/**
	 * @see com.cordinc.faraway.server.network.MessageLength#maxLength()
	 */
	@Override public long maxLength() {
		return MAX_LENGTH;
	}
	
	/**
	 * @see com.cordinc.faraway.server.network.MessageLength#bytesToLength(byte[])
	 */
	@Override public long bytesToLength(byte[] bytes) {
		if (bytes.length!=NUM_BYTES) {
			throw new IllegalStateException("Wrong number of bytes, must be "+NUM_BYTES);
		}
		return (long)(bytes[0] & 0xff);
	}
	
	/**
	 * @see com.cordinc.faraway.server.network.MessageLength#lengthToBytes(long)
	 */
	@Override public byte[] lengthToBytes(long len) {
		if (len<0 || len>MAX_LENGTH) {
			throw new IllegalStateException("Illegal size: less than 0 or greater than "+MAX_LENGTH);
		}
		return new byte[] {(byte)(len & 0xff)};
	}
}
