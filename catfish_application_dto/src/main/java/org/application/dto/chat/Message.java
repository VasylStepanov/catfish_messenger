package org.application.dto.chat;

import lombok.Getter;
import lombok.Setter;
import org.application.dto.UserDto;

@Getter
@Setter
public class Message {

    private String text;
    private UserDto author;
}
