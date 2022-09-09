package nl.abnamro.cooking.controller.mapping;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Generic class to encapsulate a message
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageResponse {
    String message;
}
