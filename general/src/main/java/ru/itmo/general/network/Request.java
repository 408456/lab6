package ru.itmo.general.network;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Objects;

@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class Request extends Sendable {
    private static final long serialVersionUID = 1L;

    public Request(boolean success, String name, Object data) {
        super(success, name, data);
    }

    public Request(String name, Object data) {
        this(true, name, data);
    }

    public String getCommand() {
        return getMessage();
    }

    @Override
    public String toString() {
        return "Request{" +
                (isSuccess() ? "" : "Ошибка при выполнении команды") +
                "command='" + getCommand() + '\'' +
                (getData() != null ? "data=" + getData() : "") +
                '}';
    }
}
