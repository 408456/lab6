package ru.itmo.lab5.comands;

/**
 * Класс-родитель для команд с именем и описанием.
 */
public abstract class Command {
    private String name, description; // Имя и описание команды

    /**
     * Конструктор класса.
     *
     * @param name        имя команды
     * @param description описание команды
     */
    public Command(String name, String description) {
        this.name = name;
        this.description = description;
    }

    /**
     * Возвращает имя команды.
     *
     * @return имя команды
     */
    public String getName() {
        return name;
    }

    /**
     * Возвращает описание команды.
     *
     * @return описание команды
     */
    public String getDescription() {
        return description;
    }

    /**
     * Переопределение метода toString.
     *
     * @return строковое представление объекта Command
     */
    @Override
    public String toString() {
        return "Command{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }

    /**
     * Абстрактный метод для выполнения команды.
     *
     * @param args аргументы команды
     * @return true, если команда выполнена успешно, иначе false
     */
    public abstract boolean execute(String[] args);
}
