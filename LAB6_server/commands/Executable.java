package commands;

import lib.General.Response;

/**
 * Интерфейс для всех выполняемых команд.
 */
public interface Executable {
  /**
   * Выполнить что-либо.
   *
   * @param arguments Аргумент для выполнения
   * @return результат выполнения
   */
  Response apply(String[] arguments, Object obj);
}
