# Twitter Clone

On va créer une application ressemblant à Twitter.

1)

Pour cela, on aura besoin d'une class tweet :

````java
class Tweet(Long id, String content, User creator, LocalDateTime createdAt)
````

Et on aura besoin d'utilisateur :

````java
class User(Long id, String userName)
````

2)

Créer un UserRepository avec des users en mémoire
Créer un TweetRepository permettant de sauvegarder des tweets

Créer un TweetService.

Les questions suivantes seront a implementer dans le service

3)

La premiere methode aura la signature :

````java
void tweet(String content, Long creatorId);
````

Elle s'occupe de sauvegarder un tweet dans le repository avec le contenu
en paramètre et le user correspondant au
creatorId

4)

Maintenant qu'un user peut créer des tweets, on veut récupérer tous ses tweets.

````java
/**
 * userId: user dont on veut lire les tweets
 * requesterId: user qui demande a voir les tweets
 */
List<Tweet> getTweetsOfUser(Long userId, Long requesterId)
````

remarque: requesterId sera utilisé plus tard a la question des users bloqués

Les tweets seront triés du plus récent au plus anciens, car on s'intéresse à ce qui est nouveau

5)

Un User peut décider de suivre d'autres users afin de voir leurs tweets.

Ajouter dans User une

````java
List<User> followed
````

Dans laquelle on ajoutera les users suivis

Créer une méthode follow(Long creatorId, Long followerId);

6)

On va créer une méthode qui va récupérer l'ensemble des tweets des personnes suivies pour un user

````java
List<Tweet> getUserFeed(Long requesterId)
````

Cette méthode ne renverra que les tweets des users suivies, tjrs triés du plus récent au plus ancien

7)

Créer une méthode pour récupérer le feed général, avec pagination.

````java
/*
        requesterId : qui demande le feed
        page: numéro de la page (0 étant la première et la valeur par défaut)
        pageSize: taille d'une page (10 étant la valeur par défaut)
 */
List<Tweet> getFeed(Long requesterId, Integer page, Integer pageSize)
````

///

8)

Ajouter un champ dans Tweet

````java
private boolean isPrivate = false;
````

Permettant de créer des tweets visibles uniquement par soit meme

9)

Un User peut bloquer un autre User et ainsi l'empêcher de voir ses tweets.

Pour cela ajouter dans User un champ

````java
private List<User> blockedUsers;
````

Un User bloqué ne saura pas qu'il est bloqué mais en ne verra aucun tweets de ceux qui l'ont bloqué.

10)

Ça arrive à tout le monde de faire des erreurs, ajouter une méthode qui permet de modifier le contenu d'un tweet.

````java
void updateTweet(Long tweetId, String content, Long requesterId)
````

Bien sûr, seul le créateur a le droit de faire cette modification (sinon une RuntimeException est levée)
Ajouter une date de mise à jour dans la class Tweet;

````java
private LocalDateTime updatedAt;
````

11)

En y réfléchissant, on veut interdir la modification d'un tweet s'il a été publié il y a plus d'1H par soucis d'ethique
Forcer cette règle avec une exception qui explique clairement l'erreur

12)

Ajouter une methode permettant de supprimer un tweet.

Mais attention, on veut faire du soft delete!

C'est à dire qu'on ne va pas supprimer réellement le tweet mais on va ajouter un champ.

````java
private boolean isDeleted = false;
````

S'assurer de ne jamais retourner un tweet supprimé lors d'un feed, il doit être considéré comme supprimé par les
utilisateurs.

Un tweet ne peut être supprimé que par son créateur

````java

void deleteTweet(Long tweetId, Long requesterId)
````

13)

Créer une fonction permettant de LIKER un tweet.

Un tweet ne peut être liké qu'une fois par personne.

Lorsqu'on récupère un tweet dans un feed, on doit voir un compteur de likes (int)

````java
void likeTweet(Long tweetId, Long requesterId)
````

14)

Similairement, on veut pouvoir DISLIKER un tweet, une seule fois par personne par tweet.

Quand on récupère un tweet, on veut voir les deux compteurs (likes et dislikes)

````java
void dislikeTweet(Long tweetId, Long requesterId)
````

15)

Maintenant, on veut récupérer les tweets les plus populaires de la journée:

Ils seront triés par la différence (like - dislike) décroissant et filtrés parmi les tweets des dernières 24h.

````java
List<Tweet> getHottestTweets(Long requesterId)
````

16)

Un user devient "influencer" quand il a obtenu 100 likes (et le reste à vie)

17)

Les tweets des influencers deviennent prioritaires dans le feed hottest 