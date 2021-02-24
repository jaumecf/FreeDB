# FreeDB

Aquesta aplicació utilitza una API de YouTube que és un fitxer .jar

Com que és un fitxer local, l'hem implemntat mitjançant una ruta fins al directori d'aquesta.
Per a poder executar aquesta aplicació en el vostre ordinador, haureu de canviar la ruta que hi ha per defecte:

Si utilitzau Linux o Mac:

implementation fileTree(dir: '/Users/jaumecampsfornari/AndroidStudioProjects/FreeDB/app/libs', include: ['*.aar', '*.jar'], exclude: [])
implementation files('/Users/jaumecampsfornari/AndroidStudioProjects/FreeDB/app/libs/YouTubeAndroidPlayerApi.jar')

Si utilitzau Windows:

implementation fileTree(dir: 'C:\\Users\\Jaume\\AndroidStudioProjects\\FreeDB\\app\\libs', include: ['*.aar', '*.jar'], exclude: [])
implementation files('C:\\Users\\Jaume\\AndroidStudioProjects\\FreeDB\\app\\libs\\YouTubeAndroidPlayerApi.jar')

Haureu de canviar pels vostres directoris fins al directori FreeDB>app>libs
