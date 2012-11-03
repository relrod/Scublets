# Scublets

[CodeBlock/rublets](https://github.com/CodeBlock/rublets) rewritten in Scala.

Scublets is a code-evaluation IRC bot which uses SELinux's `sandbox` command
to safely allow users to execute (almost-)arbitrary code on the box where the
bot resides.

**NOTE: Any security related bugs are fixed as soon as they are found, but the
developers of this bot are not responsible for anything that happens to your
computer, network, family, friends, pets, or potatos, when you run Scublets.**

# How it works - externally

```irc
< relrod> 'ruby> puts "Hello"
< scublets> Hello
< relrod> '$> id -Z
< scublets> unconfined_u:unconfined_r:sandbox_net_t:s0:c536,c662
```

# How it works - internally

The idea of rublets, the predecessor to scublets, was to provide a mechanism
for allowing users to demonistrate bits of code in realtime.

So users could throw code at the bot, the bot would know how to handle that
code, throw it to the interpreter or compiler, and send the result back to
the channel.

Scublets is not a direct port of rublets, because I wanted to use the
opportunity to use Scala's conventions where possible.

The reason for the conversion of Rublets to Scublets is simple: A lot of the
libraries I was relying on for Rublets became unmaintained by their authors,
and/or changed dramatically and the old versions were no longer supported. I
wanted something a little more self-sustaining.

# Breakpoint

[Breakpoint](https://github.com/breakpoint-eval) is a similar concept which is
also based on the general idea behind rublets. I started Breakpoint to provide
an API that does almost exactly what Rublets and Scublets do, except that it
could be used anywhere, not just on IRC. One day, far off from now, I hope that
the bots will just become dumb clients to Breakpoint's API. :)

# License

Same as Rublets, GPLv2+.
