// Copyright (c) 2020-present, Jodd Team (http://jodd.org)
// All rights reserved.
//
// Redistribution and use in source and binary forms, with or without
// modification, are permitted provided that the following conditions are met:
//
// 1. Redistributions of source code must retain the above copyright notice,
// this list of conditions and the following disclaimer.
//
// 2. Redistributions in binary form must reproduce the above copyright
// notice, this list of conditions and the following disclaimer in the
// documentation and/or other materials provided with the distribution.
//
// THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
// AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
// IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
// ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
// LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
// CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
// SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
// INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
// CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
// ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
// POSSIBILITY OF SUCH DAMAGE.

package jodd.util;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.Function;

public class StringBuilderPool {

	public static StringBuilderPool DEFAULT = new StringBuilderPool(10_000);

	private final ConcurrentLinkedQueue<StringBuilder> pool = new ConcurrentLinkedQueue<>();
	private final int maxPooledLength;

	public StringBuilderPool(final int maxPooledLength) {
		this.maxPooledLength = maxPooledLength;
	}

	private StringBuilder poll() {
		final StringBuilder sb = pool.poll();
		if (sb != null) {
			sb.setLength(0);
			return sb;
		} else {
			return new StringBuilder();
		}
	}

	private void offer(final StringBuilder sb) {
		if (sb.length() <= maxPooledLength) {
			pool.offer(sb);
		}
	}

	public <T> T withPooledBuffer(final Function<StringBuilder, T> f) {
		final StringBuilder sb = poll();
		final T res = f.apply(sb);
		offer(sb);
		return res;
	}
}
