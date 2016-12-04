package pl.edu.agh.to;

import java.util.function.Predicate;

public interface Island<Agent> {
    void removeDeadAgents(Predicate<Agent> isDead);
}
