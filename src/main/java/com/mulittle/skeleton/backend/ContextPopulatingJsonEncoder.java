package com.mulittle.skeleton.backend;

import java.nio.charset.StandardCharsets;
import java.util.Map;

import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ResolvableType;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.http.codec.json.Jackson2JsonEncoder;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.util.MimeType;

import com.mulittle.skeleton.backend.context.StepsContext;

import reactor.core.publisher.Flux;

@Component
public class ContextPopulatingJsonEncoder extends Jackson2JsonEncoder {

  public StepsContext context;
  
  @Autowired
  public ContextPopulatingJsonEncoder(StepsContext context) {
    this.context = context;
  }

  @Override
public Flux<DataBuffer> encode(Publisher<?> inputStream, DataBufferFactory bufferFactory,
    ResolvableType elementType, @Nullable MimeType mimeType, @Nullable Map<String, Object> hints) {
    System.out.println(context);
    return super.encode(inputStream, bufferFactory, elementType, mimeType, hints).doOnNext(db -> context.body = new String(extractBytes(db)));
  }

  /**
   * Extracts bytes from the DataBuffer and resets the buffer so that it is ready to be re-read by the regular
   * request sending process.
   * @param data data buffer with encoded data
   * @return copied data as a byte array.
   * Code based on: https://github.com/rewolf/blog-hmac-auth-webclient/blob/master/POST-example/src/main/java/com/github/rewolf/demo/hmacauthwebclient/client/BodyProvidingJsonEncoder.java
   */
  private byte[] extractBytes(final DataBuffer data) {
      final byte[] bytes = new byte[data.readableByteCount()];
      data.read(bytes);
      data.readPosition(0);
      return bytes;
  }
}