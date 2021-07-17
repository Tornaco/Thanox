package github.tornaco.notro.gradle.plugin.core

enum Injectors {

    LOADER_ACTIVITY_CHECK_INJECTOR('ActivityInjector', new ActivityInjector(), 'ActivityInjector');

    IClassInjector injector
    String nickName
    String desc

    Injectors(String nickName, IClassInjector injector, String desc) {
        this.injector = injector
        this.nickName = nickName
        this.desc = desc
    }
}

