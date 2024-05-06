# ProVPN

### ABOUT 
ProVPN is a simple structured AntiVPN Minecraft Spigot plugin that prevents players from joining your server through a virtual proxy/VPN!

### SUPPORTED VERSIONS
Spigot (+ Forks): 1.8x - 1.20x (latest)
Bungeecord (+ Forks)

### WHY THIS PLUGIN?
This plugin uses its own API webpage which includes no limit and makes it possible to detect and block new VPN services very easily without any struggles!

### DEVELOPER API
```java
// Create own Evfent
ProEvent event = ProEvent() {
    @Override
    public ProEventType type() {
      return ProEventType.CHECKING; // Available types: ALLOWED, CHECKING, DETECTED
    }
    
    @Override
    public void execute(Object... objects) {
        System.out.println("Checking... -> " + objects[0]);
    }
```

```java
// To add the event to the EventManger
ProEventManager.addEvent(event);
```

```java
// Call the registered events by their types 
ProEventManager.triggerEvents(ProEventType.ALLOWED, objects);
```

```java
// Check if the ip address is valid
if(!AddressUtils.isValidAddress(address)) {
// Not a valid address
return;
}

// Check if the ip address is used as a virtual proxy
if(AddressUtils.isVirtualProxy(address)) {
  // Detected using a virtual proxy
}
```
