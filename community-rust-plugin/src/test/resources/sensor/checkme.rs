fn foo() {
    if *message.get_payload() == None {
                             true
                        } else {
                             LOG_WARNING!("Payload found for Close command");
                             false
                        }
}