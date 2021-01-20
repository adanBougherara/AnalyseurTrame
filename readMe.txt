Ce projet est un analyseur offline de trame ethernet codé en Java.
Il récupère une/des trames à partir d'un fichier et permet de sauvegarder le résultat de l'analyse dans un autre fichier.

Pour ce faire les classes sont découpées en 4 packages:
-> entete qui regroupe les classes correspondant aux couches analysées (Ethernet, IP, TCP et HTTP)
-> outil qui regroupe des classes nécessaires au traitement des données (Analyseur, Conversion, OpenFile et Validite)
-> main qui contient le Main du projet ainsi que l'editeur de fenêtre (EditeurMain et EditeurFenetre)
-> exception qui contient les exceptions potentiellement levées. (CaractereInterdit et LigneIncomplete)

Le Main lance l'interface graphique (Utilisation de la bibliothèque JavaFX).
L'utilisateur peut ensuite sélectionner le fichier à traiter.
La sélection du fichier appelle l'analyseur, qui va ensuite appeler l'OpenFile. OpenFile va ensuite parser
le fichier à l'aide des différentes classes outil pour envoyer les trames sous forme d'une liste
de chaînes de caractères à l'analyseur. L'analyseur va alors traiter les trames reçues à l'aide des classes d'entete.
Enfin, l'éditeur de fenêtre affiche le résultat sur l'interface. L'utilisateur peut alors sauvegarder ce
résultat dans un fichier de son choix.

Pour plus de détail, un diagramme UML ainsi qu'une javadoc détaillée est disponible dans le dossier doc du projet.
