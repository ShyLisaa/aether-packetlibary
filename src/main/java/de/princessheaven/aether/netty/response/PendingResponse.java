/*
 * Copyright (c) 2021, Pierre Maurice Schwang <mail@pschwang.eu> - MIT
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in all
 *  copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 *  SOFTWARE.
 */

package de.princessheaven.aether.netty.response;
import de.princessheaven.aether.netty.Packet;

import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class PendingResponse<T extends Packet> {

    private final Long sent;
    private final Consumer<T> responseCallable;
    private final long timeout;

    public PendingResponse(Class<T> type, Consumer<T> responseCallable) {
        this(type, responseCallable, TimeUnit.SECONDS.toMillis(10));
    }

    public PendingResponse(Class<T> type, Consumer<T> responseCallable, long timeout) {
        this.timeout = timeout;
        this.sent = System.currentTimeMillis();
        this.responseCallable = responseCallable;
    }

    public void callResponseReceived(Packet packet) {
        responseCallable.accept((T) packet);
    }

    public boolean isExpired() {
        return System.currentTimeMillis() - sent > timeout;
    }

}
