package commands;

import managers.CollectionManager;

import lib.General.Response;


/**
 * Команда 'show'. Выводит все элементы коллекции.
 */
public class Show extends Command {
	private final CollectionManager collectionManager;

	public Show(CollectionManager collectionManager) {
		super("show", "вывести в стандартный поток вывода все элементы коллекции в строковом представлении");
		this.collectionManager = collectionManager;
	}

	/**
	 * Выполняет команду
	 * @return Успешность выполнения команды.
	 */
	@Override
	public Response apply(String[] arguments, Object obj) {
		if (!arguments[1].isEmpty()) return new Response(400, "Неправильное количество аргументов!\nИспользование: '" + getName() + "'");
		return new Response("OK",collectionManager.getCollection());
		//return new Response("OK");
	}
}
