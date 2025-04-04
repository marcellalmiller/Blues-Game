package game.observer;

import java.util.List;

/**
 * Implementations of this interface subscribe to notifications about 'EventType's from the 'IGame'
 *   instance they're associated with.
 */
public interface Observer {
  void update(EventType event, List<Object> data);
}
