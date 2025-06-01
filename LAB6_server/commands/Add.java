package commands;

import managers.CollectionManager;
import lib.General.Ticket;

import lib.General.Response;

/**
 * Команда 'add'. Добавляет новый элемент в коллекцию.
 */
public class Add extends Command {
	private final CollectionManager collectionManager;

	public Add(CollectionManager collectionManager) {
		super("add {element}", "добавить новый элемент в коллекцию");
		this.collectionManager = collectionManager;
	}

	/**
	 * Выполняет команду
	 * @return Успешность выполнения команды.
	 */
	@Override
	public Response apply(String[] arguments, Object obj) {
		if (!arguments[1].isEmpty()) return new Response(400, "Неправильное количество аргументов!\nИспользование: '" + getName() + "'");
		
		Ticket t = (Ticket) obj;
		
		if (t != null && t.validate()) {
			t.setId(collectionManager.getFreeId());
			collectionManager.add(t);
			collectionManager.addLog("add " + t.getId(), true);
			return new Response("Ticket успешно добавлен!");
		} else {
			return new Response("Поля не валидны, Ticket не создан!");
		}
	}
}
