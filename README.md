# linked-lists-mp
Authors: William Pitchford \n 
Purpose: To implement a circularly linked list with a dummy node. \n
Acknowledgements: Sam R, for initial Doubly linked lists code and node2.java. \n
Github: https://github.com/Scr0mble/linked-lists-mp
Summary: Adding a dummy node vastly simplifies the implementation of many of our iterator operations as it does not need to check for any special cases. There
technically is no front to the list, so add doesn't need to worry about inserting before the iterator, and since the list is also technically never empty, there
is no special case where we need to set this.front.
