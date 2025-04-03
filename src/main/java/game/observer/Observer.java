package game.observer;

import java.util.List;

public interface Observer {
  void update(EventType event, List<Object> data);
}
