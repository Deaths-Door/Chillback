use std::{ops::Deref, time::Duration};

use once_cell::sync::Lazy;
use r2d2::{Pool,PooledConnection};
use r2d2_sqlite::SqliteConnectionManager;

static CACHE_INSTANCE : Lazy<Pool<SqliteConnectionManager>> = Lazy::new(||{
    let manager = SqliteConnectionManager::file("cache.db");

    Pool::builder()
        .idle_timeout(Duration::from_secs(60).into())
        .build(manager)
        .unwrap()
});

pub fn cache_instance<'a>() -> PooledConnection<SqliteConnectionManager> {
    // TODO : Check if unwrap is safe always
    crate::database::CACHE_INSTANCE.deref().get().unwrap()
}