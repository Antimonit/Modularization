# Modularization

The project shows how our monolithic application could be modularized into smaller, independent 
modules.

Current application consists of a single, monolithic **app** module. The goal of the modularization
process is to identify independent features in the **app** module and extract features to standalone 
modules.                                               

## Migration towards modular architecture

It is generally advised to start by renaming current **app** module to something like 
**core**/**common**/**base** that will contain all the original code and change it's module type 
from `com.android.application` to `com.android.library` type. 

Then create a new **app** module of type `com.android.application` that will be just a thin wrapper
dependent on other modules. This module should not contain much code, possibly just configuration of
some global libraries like `Crashlytics`, `Timber`, `ThreeTenABP` and global DI. 
 
## Extracting modules

As we identify independent features and and extract relevant code to a standalone modules we also 
have to define dependencies between the modules. 

>Keep in mind that modules cannot form circular dependencies.

### Low-level modules
Some of the modules might be completely standalone and require no dependencies to other modules.
Good example is **network** module. Such module only depends on external libraries and provide
an interface to perform network requests that is consumed by other libraries. Many other modules 
will probably depend on such module.

### High-level modules
On the other hand, some other modules might be dependent on other module but no other module 
depends on it. Good example is **intro** module. 

Because separate features don't depend on each other and

## Proposed modules

As explained in the previous section, we will need:
- light **app** module that will include all other modules
- heavy common **base** module that all features would depend on - **base**.

Then the most obvious split is to separate the application into 2 main feature modules:
- **borrower**
- **investor**

These roles have minimal overlap in the codebase. There are plans to add more roles in the future 
and these roles will also contribute separate modules.

- **intro** can also be extracted to a standalone feature as nothing else is dependent on it
- **onboarding** might be also a feature worth extracting. It might not be so simple because

Then we have low-level modules
- **network**
- **navigation**


## Things to decide on

### Database access

Should it be in a separate module or should each module define their own database?
- Separation by modules 
  - ➕ Cleaner encapsulation of each module
  - ➖ Cannot perform joins across the modules
- Single central database
  - ➖ Provides a way to modify data of another module
  - ➕ Allows joins across modules
   
### Dependency Injection

Dependency injection must be separated by modules as we need to provide some dependencies 
only within the 
And there is no point in providing dependencies across modules.

Needs more research. 

