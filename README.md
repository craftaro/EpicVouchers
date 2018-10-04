## EpicVouchers
EpicVouchers is the best solution for vouchers on any server with amazing features updated everyday by me (Songoda).</br>
Quality, performance, and support are my priorities for this resource. Purchase it for $2.99 (sometimes cheaper with sales).
> **Note:**  Please consider purchasing this resource on Spigot if you want to really support me.
</br>
 
## Developers
Here is an example with built-in methods for developers that want to use the EpicVouchers API for their own resources.</br>
This is a kinda long example so think about that too but I am sure that you will understand the usage of it quick and easy.
```ruby
@EventHandler
public void redeemListener(VoucherRedeemEvent event) {
# Returns the player that redeemed the voucher.
Player player = event.getPlayer();
# Returns the name of the redeemed voucher.
String voucher = event.getVoucher();
# Returns the ItemStack of the redeemed voucher.
ItemStack item = event.getItem();
# Returns if the voucher was redeemed manual or not.
boolean manual = event.getManual();
# Returns if the event is cancelled or not.
boolean cancelled = event.isCancelled();
# Get a list of handlers for this event.
HandlerList handlers = event.getHandlers();
# Cancel the event with a boolean.
event.isCancelled(true);
}

@EventHandler
public void forceListener(ForceRedeemEvent event) {
# Returns the player that redeemed the voucher.
Player player = event.getPlayer();
# Returns the name of the redeemed voucher.
String voucher = event.getVoucher();
# Returns the amount of vouchers that were redeemed.
int amount = event.getAmount();
# Returns the sender that forced the players.
CommandSender sender = event.getSender();
# Returns if the event is cancelled or not.
boolean cancelled = event.isCancelled();
# Get a list of handlers for this event.
HandlerList handlers = event.getHandlers();
# Cancel the event with a boolean.
event.isCancelled(true);
}

@EventHandler
public void receiveListener(VoucherReceiveEvent event) {
# Returns the player that received the voucher.
Player player = event.getPlayer();
# Returns the name of the received voucher.
String voucher = event.getVoucher();
# Returns the ItemStack of the received voucher.
ItemStack item = event.getItem();
# Returns the amount of vouchers that were received.
int amount = event.getAmount();
# Returns the sender that gave the vouchers.
CommandSender sender = event.getSender();
# Returns if the event is cancelled or not.
boolean cancelled = event.isCancelled();
# Get a list of handlers for this event.
HandlerList handlers = event.getHandlers();
# Cancel the event with a boolean.
event.isCancelled(true);
}
```