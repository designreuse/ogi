Properties
===
Chargement des properties
---

L'application gère deux type de fichiers properties :
  
  * environment.properties : properties pouvant changer entre les environnements
  * functionnal.properties : properties ne dépendant pas de l'environnement. Purement fonctionnel
  
Les batchs disposent de properties qui leur sont propres et les définissent dans

  * environment-batch.properties
  * functional-batch.properties
  
Comme le projet **ogi-batchs** dépend de **ogi-services** , l'ensemble des properties des deux projets existent dans le contexte Spring. Chaque projet défini sont PropertyPlaceholderConfigurer.
Le PropertyPlaceholderConfigurer de **ogi-services** doit avoir la propriété ignoreUnresolvablePlaceholders à true pour qu'il ignore les properties qu'il ne connait pas.
L'ordre de chargement est important des fichiers contexte et important : le contexte de **ogi-services** doit être chargé avant celui de **ogi-batchs**.

Confidentialité
---
Les mots de passe dans les properties sont chiffrés via l'algorithme AES.
Le PropertyPlaceholderConfigurer est une classe spéciale : *fr.jerep6.ogi.framework.spring.CryptablePropertyPlaceholderConfigurer*
Il faut l'initialiser avec la clé de l'algorithme AES pour qu'il puisse déchiffrer les properties. Pour cela, il suffit de définir la propriété système **propertyConfigurerPassword**

La valeur d'une property chiffrée doit être de la forme <code>ENC(RESULTAT ALGO AES)</code>.

Exemple :

	partner.connect.login = loginTEST
	partner.connect.pwd = ENC(0gWCFWRTyg90mP+v5Bv7Q==)

  
Spring Profiles
===
L'application gère 2 profiles :

 1. web : datasource jndi
 2. batch : datasource c3p0, ajout du PropertyPlaceholderConfigurer pour le batch (environment-batch.properties, functional-batch.properties)
 
Il est obligatoire de spécifier un profile avant de lancer l'application. 

  * Le web.xml défini l'utilisation du profile **web**. Il n'est donc pas nécessaire de rajouter l'option au lancement du serveur web.
  * En revanche, pour les batchs, il faut ajouter le paramètre <code>-Dspring.profiles.default=batch</code> à la ligne de commande.



Serveur WEB
===
Ajouter la propriété suivante à la commande de lancement :
<code>-DpropertyConfigurerPassword="clé secrète"</code>

Batch
===Ajouter la propriété suivante à la commande de lancement :
<code>-Dspring.profiles.default=batch</code>