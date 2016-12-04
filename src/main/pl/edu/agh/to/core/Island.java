package pl.edu.agh.to.core;

import java.util.function.Predicate;

public interface Island<Agent> {
    void removeDeadAgents(Predicate<Agent> isDead);
}
