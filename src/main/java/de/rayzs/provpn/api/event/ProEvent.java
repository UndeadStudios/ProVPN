package de.rayzs.provpn.api.event;

public interface ProEvent {

    ProEventType type();
    void execute(Object... objects);
}
