# Unnamed Client
A FOSS mcp hacked (ghost/automation is a priority) client for minecraft intended for at least half standard users.  
ScriptApi and REPL in game is supported (Soon).  

However, a Scala REPL run configuration was included in project files.
simply run Scala REPL and type
```
import net.unnamed.utils.REPLHelper
 
REPLHelper.startMinecraft()
```
will launch Minecraft and have the REPL stay available for you.

Very different client base, have fun skidding.
## Building on *nix
### Prerequisites
`net-misc/curl` `sys-apps/coreutils` `app-arch/unzip` `dev-python/virtualenv`.  
A instance of 1.8.8 is presented in the default path ready for mcp.      
If located elsewhere, `mount --rbind` or `overlayfs` could be an option, MultiMC doesn't seem to be nice here.

### Setup

run `setup-scripts/project-setup.sh`
It will grab mcp/optifine, setup mcp and project.  

### Build

Launch idea, open `idea-project`.
To build the client, `Build -> Build Artifacts`.  
A launch profile is already include in the project.  
You might have to change the scala sdk version on your machine.

### Install

`setup-scripts/install.sh <directory - default: ~/.minecraft>` copies the built jar and json.

## Notes
It is recommended that you should audit the code in this repo.  
The repository stores patches to build the development environment and scripts to automate this process.  
Any help is appreciated, especially those can optimize workflow.  
The client jar is very big right now, not sure if I would plan to put the dependencies in the json in the future 
(you are going to download the libs anyway if you want to use the client **LEGALLY**).  