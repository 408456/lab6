package ru.itmo.general.network;

import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
public abstract class Sendable implements Serializable {
    protected final boolean success;
    protected final String message;
    protected final Object data;
    protected Integer userId;

    public Sendable(boolean success, String name, Object data) {
        this.success = success;
        this.message = name;
        this.data = data;
    }
}
