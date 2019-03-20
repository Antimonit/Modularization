# Dagger / Dagger-Android / Koin

This chapter discusses three approaches to implementation of the Dependency Injection pattern.
- Pure Dagger implementation
- Dagger with extra utilities provided by Dagger-Android library
- Koin library built for Kotlin-based applications

## Scopes
- Singleton
- Custom scope
- No scope


## Reasoning
Objects should not know how they are injected.

## Dagger

Using standard Dagger approach we manage components all by ourselves:
```
override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    App.investmentComponent(arguments!!.getParcelable<Investment>(INVESTMENT_KEY).id).inject(this)
}
```

### Dependent components vs. Subcomponents


## Dagger-Android
Using Dagger-Android we can just write:
```
override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    AndroidInjection.inject(this)
}
```
which is out-of-the-box included in `DaggerAppCompatActivity`, `DaggerFragment`, `DaggerService`, `DaggerDialogFragment`, ...

https://github.com/marcosholgado/dagger-playground/commit/aecc4a71eec8695509ea81a892a8835b5511ecc9
https://github.com/marcosholgado/dagger-playground/commit/7cd13ac8e403551771282b0aa7f554d885e23bd5

## Koin
In contrast to Dagger, which builds the whole dependency graph during compilation, Koin does not
perform graph validation during compilation.
- Much faster build times
- Much much much simpler configuration
- Dependency Graph may not be valid 

