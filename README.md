# Unnamed Client
A FOSS mcp hacked client for minecraft intended for at least half standard users.
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
It is recommend that you should audit the code in this repo.  
The repository stores patches to build the development environment and scripts to automate this process.  
Any help is appereciated, especially those can optimize workflow.