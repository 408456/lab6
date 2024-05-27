package ru.itmo.general.comands;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

/**
 * Абстрактный класс команды.
 */
@Getter
@AllArgsConstructor
public abstract class Command implements Describable, Executable {

    private final String name; /**< Название команды */
    private final String description; /**< Описание команды */

    /**
     * Возвращает сообщение об ошибке при неправильном использовании команды.
     *
     * @return сообщение об ошибке
     */
    public String getUsingError() {
        return "Неверное количество аргументов!\nUsage: '" + getName() + getDescription() + "'";
    }

    /**
     * Переопределение метода equals для сравнения объектов класса Command.
     *
     * @param o объект, с которым сравнивается текущий объект
     * @return true, если объекты равны, в противном случае - false
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Command command = (Command) o;
        return Objects.equals(name, command.name) && Objects.equals(description, command.description);
    }

    /**
     * Переопределение метода hashCode для вычисления хэш-кода объекта.
     *
     * @return хэш-код объекта
     */
    @Override
    public int hashCode() {
        return Objects.hash(name, description);
    }

    /**
     * Переопределение метода toString для представления объекта в виде строки.
     *
     * @return строковое представление объекта
     */
    @Override
    public String toString() {
        return "Command{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
