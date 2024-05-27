package ru.itmo.general.comands;

import ru.itmo.general.managers.CollectionManager;
import ru.itmo.general.network.Request;
import ru.itmo.general.network.Response;

/**
 * Команда для сохранения коллекции в файл.
 */
public class Save extends Command {
    private final CollectionManager collectionManager;

    public Save(CollectionManager collectionManager) {
        super("save", " - сохранить коллекцию в файл");
        this.collectionManager = collectionManager;
    }

    @Override
    public Response execute(Request request) {
        try {
            collectionManager.saveCollection();
            return new Response(true, "Коллекция успешно сохранена!");
        } catch (Exception e) {
            return new Response(false, "Произошла ошибка при сохранении коллекции: " + e.getMessage());
        }
    }
}
