Properties
===
Chargement des properties
---

L'application gère deux types de fichiers properties :
  
  * environment.properties : properties pouvant changer entre les environnements
  * functionnal.properties : properties ne dépendant pas de l'environnement. Purement fonctionnel
  
Les batchs disposent de properties qui leur sont propres et les définissent dans

  * environment-batch.properties
  * functional-batch.properties
  
Comme le projet **ogi-batchs** dépend de **ogi-services** , l'ensemble des properties des deux projets existent dans le contexte Spring. Chaque projet défini son PropertyPlaceholderConfigurer.
Le PropertyPlaceholderConfigurer de **ogi-services** doit avoir la propriété ignoreUnresolvablePlaceholders à true pour qu'il ignore les properties qu'il ne connait pas.
L'ordre de chargement des fichiers contexte et important : le contexte de **ogi-services** doit être chargé avant celui de **ogi-batchs**.

Confidentialité
---
Les mots de passe dans les properties sont chiffrés via l'algorithme AES.
Le PropertyPlaceholderConfigurer est donc une classe spéciale : *fr.jerep6.ogi.framework.spring.CryptablePropertyPlaceholderConfigurer*.
Il faut l'initialiser avec la clé de l'algorithme AES pour qu'elle puisse déchiffrer les properties. Pour cela, il suffit de définir la propriété système **propertyConfigurerPassword**

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
===
Ajouter la propriété suivante à la commande de lancement :
<code>-Dspring.profiles.default=batch</code>




Lancer OGI en local
===

MySQL
---	
Création d'une base mysql à travers un container docker
```
docker run --name mysql-ogi -p 3306:3306 -e MYSQL_ROOT_PASSWORD=root -e MYSQL_DATABASE=ogi -e MYSQL_USER=ogi -e MYSQL_PASSWORD=ogi -d mysql:5.5
```
Explications : 
 * **MYSQL_USER, MYSQL_PASSWORD** : Création d'un nouvel utilisateur avec son mot de passe. Cet utilisateur se verra attribuer toutes les permissions sur la base MYSQL_DATABASE
 * **MYSQL_DATABASE** : Nom de la base de données ) créer.
 * **MYSQL_ROOT_PASSWORD** : mot de passe du compte root

Relancer le container docker suite à un arrêt :
```
docker start mysql-ogi
```