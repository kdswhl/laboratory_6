package commands;

import managers.CollectionManager;

import lib.General.Venue;
import lib.General.Ticket;
import lib.General.Response;


public class RemoveAnyByVenue extends Command {

	private final CollectionManager collectionManager;

	public RemoveAnyByVenue(CollectionManager collectionManager) {
		super("remove_any_by_character {character}", "удалить из коллекции один элемент, значение поля character которого эквивалентно заданному");

		this.collectionManager = collectionManager;
	}

	/**
	 * Выполняет команду
	 * @return Успешность выполнения команды.
	 */
	@Override
	public Response apply(String[] arguments, Object obj) {
		if (!arguments[1].isEmpty()) return new Response(400, "Неправильное количество аргументов!\nИспользование: '" + getName() + "'");
		
		var ch = (Venue)obj;
		Ticket d = null;
		for (var e : collectionManager.getCollection()) {
			if ((e.getVenue() == null && ch == null) || (e.getVenue() != null && ch != null && e.getVenue().equals(ch))) {
				d = e;
				break;
			}
		}
		if (d != null) {
			collectionManager.remove(d.getId());
			collectionManager.addLog("remove " + d.getId(), true);
			collectionManager.update();
			return new Response("Ticket успешно удалён!");
		} else {
			return new Response("Не найдена такая Venue");
		}
	}
}
