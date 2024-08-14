package org.mju_likelion.festival.announcement.util.deserializer;

import static org.mju_likelion.festival.common.exception.type.ErrorType.INVALID_REQUEST_BODY_ERROR;
import static org.mju_likelion.festival.common.util.field_wrapper.FieldWrapper.getStringFieldWrapper;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import org.mju_likelion.festival.announcement.dto.request.UpdateAnnouncementRequest;
import org.mju_likelion.festival.common.exception.BadRequestException;

/**
 * 공지사항 수정 요청 Deserializer
 * <p>
 * title, content, imageUrl을 역직렬화한다.
 * <p>
 * 클라이언트가 null 을 전송한 것인지 아니면 필드 자체가 없는 것인지 구분하기 위해 JsonNode 의 isNull() 메서드를 사용한다.
 */
public class UpdateAnnouncementRequestDeserializer extends
    StdDeserializer<UpdateAnnouncementRequest> {

  public UpdateAnnouncementRequestDeserializer() {
    this(null);
  }

  public UpdateAnnouncementRequestDeserializer(Class<?> vc) {
    super(vc);
  }

  @Override
  public UpdateAnnouncementRequest deserialize(JsonParser jp, DeserializationContext ctxt) {
    try {
      JsonNode node = jp.getCodec().readTree(jp);
      UpdateAnnouncementRequest request = new UpdateAnnouncementRequest();

      if (node.has("title")) {
        request.setTitle(node.get("title").isNull() ? null : node.get("title").asText());
      }
      if (node.has("content")) {
        request.setContent(node.get("content").isNull() ? null : node.get("content").asText());
      }
      request.setImageUrl(getStringFieldWrapper(node, "imageUrl"));

      return request;
    } catch (Exception e) {
      throw new BadRequestException(INVALID_REQUEST_BODY_ERROR);
    }
  }
}
