package commands;

import managers.CollectionManager;

import lib.General.Response;

/**
 * Команда 'remove_by_id'. Удаляет элемент из коллекции.
 */
public class RemoveById extends Command {

	private final CollectionManager collectionManager;

	public RemoveById(CollectionManager collectionManager) {
		super("remove_by_id ID", "удалить элемент из коллекции по ID");

		this.collectionManager = collectionManager;
	}

	/**
	 * Выполняет команду
	 * @return Успешность выполнения команды.
	 */
	@Override
	public Response apply(String[] arguments, Object obj) {
		if (arguments[1].isEmpty()) return new Response(400, "Неправильное количество аргументов!\nИспользование: '" + getName() + "'");
		int id = -1;
		try { id = Integer.parseInt(arguments[1].trim()); } catch (NumberFormatException e) { return new Response(400, "ID не распознан"); }
		
		if (collectionManager.byId(id) == null || !collectionManager.getCollection().contains(collectionManager.byId(id)))
			return new Response(400, "Не существующий ID");
		collectionManager.remove(id);
		collectionManager.addLog("remove " + id, true);
		collectionManager.update();
		return new Response("Ticket успешно удалён!");
	}
}
