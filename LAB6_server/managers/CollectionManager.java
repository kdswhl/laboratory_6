package managers;

import java.time.Instant;
import java.util.LinkedList;
import java.util.Stack;
import lib.General.Ticket;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Оперирует коллекцией.

 */
public class CollectionManager {
	private int currentId = 1;
	private Map<Integer, Ticket> tickets = new HashMap<>();
	private ArrayDeque<String> logStack = new ArrayDeque<String>();

	private LinkedList<Ticket> collection = new LinkedList<Ticket>();
	private LinkedList<Ticket> collectionDie = new LinkedList<Ticket>();
	private LocalDateTime lastInitTime;
	private LocalDateTime lastSaveTime;
	private final DumpManager dumpManager;
	public boolean isAscendingSort;

	public CollectionManager(DumpManager dumpManager) {
		this.lastInitTime = null;
		this.lastSaveTime = null;
		this.dumpManager = dumpManager;
	}

	/**
	 * @return коллекция.
	 */
	public LinkedList<Ticket> getCollection() {
		return collection;
	}

	/**
	 * @return Последнее время инициализации.
	 */
	public LocalDateTime getLastInitTime() {
		return lastInitTime;
	}

	/**
	 * @return Последнее время сохранения.
	 */
	public LocalDateTime getLastSaveTime() {
		return lastSaveTime;
	}

	/**
	 * Получить дракона по ID
	 */
	public Ticket byId(int id) { return tickets.get(id); }

	/**
	 * Содержит ли колекции дракона
	 */
	public boolean isСontain(Ticket e) { return e == null || byId(e.getId()) != null; }

	/**
	 * Получить свободный ID
	 */
	public int getFreeId() {
		while (byId(currentId) != null || byDieId(currentId) != null)
			if (++currentId < 0)
				currentId = 1;
		return currentId;
	}


	public Ticket byDieId(int id) { try{for (var e:collectionDie)if (e.getId()==id)return e;return null;} catch (IndexOutOfBoundsException e) { return null; } }

	/**
	 * Добавляет дракона по ID из удалённых
	 */
	public boolean add(int id) {
		Ticket ret = byDieId(id);
		if (ret == null) return false;
		tickets.put(ret.getId(), ret);
		collection.add(ret);
		collectionDie.remove(ret);
		update();
		return true;
	}

	/**
	 * Добавляет дракона
	 */
	public boolean add(Ticket d) {
		if (d == null) return false;
		tickets.put(d.getId(), d);
		collection.add(d);
		update();
		return true;
	}

	/**
	 * @return true в случае успеха.
	 */
	public boolean swap(int id, int repId) {
		var e = byId(id);
		var re = byId(repId);
		if (e == null) return false;
		if (re == null) return false;
		var ind = collection.indexOf(e);
		var rind = collection.indexOf(re);
		if (ind < 0) return false;
		if (rind < 0) return false;
		
		e.setId(repId);
		re.setId(id);
		
		tickets.put(e.getId(), e);
		tickets.put(re.getId(), re);
		
		// addLog("swap " + id + " " + repId , false);
		// replacement
		update();
		return true;
	}

	/**
	 * Удаляет по ID
	 */
	public boolean remove(int id) {
		Ticket ret = byId(id);
		if (ret == null) return false;
		var ind = collection.indexOf(ret);
		if (ind < 0) return false;
		collection.remove(ret);
		collectionDie.add(ret);
		update();
		return true;
	}

	/**
	 * Отменяет n команд
	 */
	public int undo(int count) {
		var tmpLog = new LinkedList<String>();
		try {
			for (var i = 0 ; i < count ; i++) {
				if (logStack.size() == 0) return i;
				for (var j = logStack.size() - 1 ; j>=0 ; j--) {
					var s = logStack.pop();
					if (s.equals("+")) break;
					int n = -1;
					int n2 = -1;
					try { n = Integer.parseInt((s + " ").split(" ")[1]); } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) { }
					try { n2 = Integer.parseInt((s + " ").split(" ")[2]); } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) { }
					switch ((s + " ").split(" ")[0]) {
						case "add":
							if (!remove(n)) return i;
							tmpLog.push("remove " + n);
							break;
						case "remove":
							if (!add(n)) return i;
							tmpLog.push("add " + n);
							break;
						case "reorder":
							tmpLog.push("reorder");
							isAscendingSort ^= true;
							update();
							break;
						case "swap":
							if (!swap(n, n2)) return i;
							tmpLog.push("swap " + n + " " + n2);
							break;
						default:
							System.err.println("Undef command: " + s);
							System.out.println("Undef command: " + s);
							System.exit(1);
							return i;
					}
				}
			}
		} finally {
			addLog("", true);
			while (tmpLog.size()>0)
				addLog(tmpLog.removeLast(), false);
		}
		return count;
	}

public void deb2(long t) {
	System.out.println("    ===" + t+ "===");
	System.out.println(this);
	for (var ee : logStack)
		System.out.println("    "+t+"_" + ee);
	System.out.println("    ======");
	
}

	/**
	 * Создает транзакцию или добавляет операцию в транзакцию
	 */
	public void addLog(String cmd, boolean isFirst) {
		if (isFirst)
			logStack.push("+");
		if (!cmd.equals(""))
			logStack.push(cmd);
	}

	/**
	 * @return true в случае успеха.
	 */
	public boolean lastAddDragon(Ticket e) {
		if (isСontain(e)) return false;
		tickets.put(e.getId(), e);
		collection.add(e);
		return true;
	}

	/**
	 * Фиксирует изменения коллекции
	 */
	public void update() {
		Collections.sort(collection);
		if (isAscendingSort) Collections.reverse(collection);
	}

	/**
	 * Сохраняет коллекцию в файл
	 */
	public void saveCollection() {
		dumpManager.writeCollection(collection, collectionDie, logStack);
		lastSaveTime = LocalDateTime.now();
	}

	/**
	 * Загружает коллекцию из файла.
	 * @return true в случае успеха.
	 */
	public boolean loadCollection() {
		tickets.clear();
		collection.clear();
		collectionDie.clear();
		logStack.clear();
		dumpManager.readCollection(collection, collectionDie, logStack);
		lastInitTime = LocalDateTime.now();
		for (var e : collection)
			if (byId(e.getId()) != null) {
				tickets.clear();
				collection.clear();
				collectionDie.clear();
				logStack.clear();
				
				return false;
			} else {
				if (e.getId()>currentId) currentId = e.getId();
				tickets.put(e.getId(), e);
			}
		for (var e : collectionDie)
			if (byId(e.getId()) != null) {
				tickets.clear();
				collection.clear();
				collectionDie.clear();
				logStack.clear();
				return false;
			} else {
				if (e.getId()>currentId) currentId = e.getId();
				tickets.put(e.getId(), e);
			}
		update();
		return true;
	}

	@Override
	public String toString() {
		if (collection.isEmpty()) return "Коллекция пуста!";

		StringBuilder info = new StringBuilder();
		for (Ticket ticket : collection) {
			info.append(ticket+"\n\n");
		}
		return info.toString().trim();
	}
}
