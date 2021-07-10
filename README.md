<!--suppress HtmlDeprecatedAttribute -->
<div align="center">
<img alt="EpicVouchers" src="https://cdn2.songoda.com/products/epicvouchers/JXt9xdI2AsSKfhFeUgy8fY14pPUAwhLRxJE5zeet.gif">

# EpicVouchers

**Create vouchers that players can claim for rewards with seemingly infinite possibilities.**
**Customize your server’s vouchers through an intuitive in-game editor.**

<!-- Shields -->
[![Discord](https://img.shields.io/discord/293212540723396608?color=7289DA&label=Discord&logo=discord&logoColor=7289DA)](https://discord.gg/songoda)
[![Patreon](https://img.shields.io/badge/-Support_on_Patreon-F96854.svg?logo=patreon&style=flat&logoColor=white)](https://www.patreon.com/join/songoda)
<br>
[![Latest version](https://img.shields.io/github/v/tag/songoda/EpicVouchers?include_prereleases&label=Latest&logo=github&labelColor=black)](https://songoda.com/marketplace/product/25)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=songoda_EpicVouchers&metric=alert_status)](https://sonarcloud.io/dashboard?id=songoda_EpicVouchers)
[![GitHub last commit](https://img.shields.io/github/last-commit/songoda/EpicVouchers?label=Last+commit)](https://github.com/songoda/EpicVouchers/commits)
<br>
[![bStats Servers](https://img.shields.io/bstats/servers/4209?label=Servers)](https://bstats.org/plugin/bukkit/EpicVouchers/4209)
</div>

## Table of Contents

* [Introduction](#introduction)
* [Marketplace](#marketplace)
* [Documentation](#documentation)
* [Developers API](#developers-api)
* [Support](#support)
* [Suggestions](#suggestions)

## Introduction

EpicVouchers is simple to use voucher plugin, that gives you the ability to reward players automatically without needing
to give them permission to a command or manually run a command on a player yourself. This plugin is perfect for cosmetic
rewards, selling in-game ranks, crate and kit rewards, unique shop designs, etc. The plugin also features an in-game GUI
that allows you to create, edit and delete vouchers without needing to configurate the files, or restart/reload the
server to apply the changes. It's a perfect plugin for production servers, and gives you unlimited ways to reward your
loyal players easily.

## Marketplace

You can visit [our marketplace](https://songoda.com/marketplace/product/25) to download EpicVouchers as well as take a
look at many other fantastic plugins which are sure to catch your eye.

## Documentation

You can find all the information about EpicAnchors, including dependencies, commands, permissions and incompatible
plugins on [our wiki](https://wiki.songoda.com/Epic_Vouchers).

Feel free to also contribute to the wiki as a way to help others in the community with using the plugin.

## Developers API

Here is an example with built-in methods for developers that want to use the EpicVouchers API for their own resources.
This is a pretty long example, so take that into consideration when looking at the example, but I am sure that you will
understand the usage of it quickly.

```java
public class VouchersExample implements Listener {
  @EventHandler
  public void onRedeem(VoucherRedeemEvent event) {
    // Returns the player that redeemed the voucher.
    Player player = event.getPlayer();
    // Returns the name of the redeemed voucher.
    String voucher = event.getVoucher();
    // Returns the ItemStack of the redeemed voucher.
    ItemStack item = event.getItem();
    // Returns if the voucher was redeemed manual or not.
    boolean manual = event.getManual();
    // Returns if the event is cancelled or not.
    boolean cancelled = event.isCancelled();
    // Get a list of handlers for this event.
    HandlerList handlers = event.getHandlers();
    // Cancel the event with a boolean.
    event.setCancelled(true);
  }

  @EventHandler
  public void onForceRedeem(ForceRedeemEvent event) {
    // Returns the player that redeemed the voucher.
    Player player = event.getPlayer();
    // Returns the name of the redeemed voucher.
    String voucher = event.getVoucher();
    // Returns the amount of vouchers that were redeemed.
    int amount = event.getAmount();
    // Returns the sender that forced the players.
    CommandSender sender = event.getSender();
    // Returns if the event is cancelled or not.
    boolean cancelled = event.isCancelled();
    // Get a list of handlers for this event.
    HandlerList handlers = event.getHandlers();
    // Cancel the event with a boolean.
    event.setCancelled(true);
  }

  @EventHandler
  public void onReceive(VoucherReceiveEvent event) {
    // Returns the player that received the voucher.
    Player player = event.getPlayer();
    // Returns the name of the received voucher.
    String voucher = event.getVoucher();
    // Returns the ItemStack of the received voucher.
    ItemStack item = event.getItem();
    // Returns the amount of vouchers that were received.
    int amount = event.getAmount();
    // Returns the sender that gave the vouchers.
    CommandSender sender = event.getSender();
    // Returns if the event is cancelled or not.
    boolean cancelled = event.isCancelled();
    // Get a list of handlers for this event.
    HandlerList handlers = event.getHandlers();
    // Cancel the event with a boolean.
    event.setCancelled(true);
  }
}
```

## Support

If you encounter any issues while using the plugin, feel free to create a ticket
on [our support desk](https://support.songoda.com).

## Suggestions

For suggestions about features you think should be added to the plugin to increase its functionality, feel free to
create a thread over on [our feedback site](https://feedback.songoda.com).
