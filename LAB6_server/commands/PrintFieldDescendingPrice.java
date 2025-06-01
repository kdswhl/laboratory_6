package commands;

import managers.CollectionManager;
import java.util.TreeSet;
import lib.General.Response;

public class PrintFieldDescendingPrice extends Command {
	private final CollectionManager collectionManager;

	public PrintFieldDescendingPrice(CollectionManager collectionManager) {
		super("print_field_descending_price", "вывести значения поля price всех элементов в порядке убывания");
		this.collectionManager = collectionManager;
	}

	/**
	 * Выполняет команду
	 * @return Успешность выполнения команды.
	 */
	@Override
	public Response apply(String[] arguments, Object obj) {
		if (!arguments[1].isEmpty()) return new Response(400, "Неправильное количество аргументов!\nИспользование: '" + getName() + "'");
		var beNull = false;
		var ts = new TreeSet<Double>();
		for (var e : collectionManager.getCollection()) {
			if (e.getPrice() == null)
				beNull = true;
			else
				ts.add(e.getPrice());
		}
		var s="";
		if (beNull)
			s=" null";
		for (var e : ts)
			s+=" " + e;
		return new Response(s);
	}
}
