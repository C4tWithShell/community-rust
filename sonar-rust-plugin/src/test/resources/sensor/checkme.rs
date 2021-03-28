
use std::convert::TryInto;
use std::future::Future;
use std::rc::Rc;



fn foo() -> u8
{ Box::new(move |state : Rc<RefCell<OpState>>, bufs: BufVec| -> Op {
        let mut b = 42;
    })
}


