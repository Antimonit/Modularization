# Modularization

The project shows how our monolithic application could be modularized into smaller, independent 
modules.

Current application consists of a single, monolithic **app** module. The goal of the modularization
process is to identify independent features in the **app** module and extract features to standalone 
modules.                                               

There are two approaches to modularization of an application. We can split an application: 
- **Vertically into layers** - View / ViewModel / Repository / Model / Data layers. Each layer has
clearly defined responsibilities and dependencies. For example, only View layer should depend on 
Android framework or another example, only Data layer should depend on Retrofit. Each layer then
spans multiple features.
- **Horizontally into features** - Login / Investor Market / Borrower portfolio / Settings / etc. 
Each feature implements complete flow throughout one specific feature. Each feature then spans 
multiple layers.

In my personal opinion both approaches have valid points. For an app with features that are closely
related to each other, separation into features might not make much sense and in the end we might
end up with a single feature module just like **app**. Even without modules, we separate the code
by several layers.

In case of our application it makes more sense to split primarily by features as there are multiple 
flows through the application that are completely unrelated to each other. It might be beneficial
to separate some extra modules by layers, such as Network module.

## Migration towards modular architecture

It is generally advised to start by renaming current **app** module to something like 
**core**/**common**/**base** that will contain all the original code and change it's module type 
from `com.android.application` to `com.android.library` type. 

Then create a new **app** module of type `com.android.application` that will be just a thin wrapper
dependent on other modules. This module should not contain much code, possibly just configuration of
some global libraries like `Crashlytics`, `Timber`, `ThreeTenABP` and global DI. This is a good 
place where to keep `Application` class.
 
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

## Proposed modules

As explained in the previous section, we will need:
- light **app** module that will include all other modules,
- heavy common **base** module that all features would depend on.

Then the most obvious split is to separate the application into 2 main feature modules:
- **borrower**,
- **investor**

These roles have minimal overlap in the codebase. There are plans to add more roles in the future 
and these roles will also contribute separate modules.

- **intro** can also be extracted to a standalone feature as nothing else is dependent on it,
- **onboarding** is difficult to tell if it is worth or even possible to extract. Currently, 
onboarding processes for both Investor and Borrower are quite hard-coded and lot of code is 
duplicated. Once the dynamic onboarding is implemented on the backend, it might become more clear.

Then we have low-level modules
- **network**
- **navigation** - interface defined as low-level but implementation provided by high-level module.

## Things to decide on

### Database access

Should it be in a separate module or should each module define their own database?
- Separation by modules 
  - ➕ Cleaner encapsulation of each module
  - ➖ Cannot perform joins across the modules
- Single central database
  - ➖ Provides a way to modify data of another module
  - ➕ Allows joins across modules

In my opinion there is nothing wrong with defining separate databases for each module as we don't
perform any joins across modules.

### Dependency Injection

Dependency injection must be part of each feature as we need to provide some dependencies 
only within the scope of the feature. 

Needs more research. 

