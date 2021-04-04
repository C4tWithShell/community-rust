// Copyright 2018-2021 the Deno authors. All rights reserved. MIT license.

use crate::colors;
use crate::fs_util::resolve_from_cwd;
use deno_core::error::custom_error;
use deno_core::error::uri_error;
use deno_core::error::AnyError;
use deno_core::serde::Deserialize;
use deno_core::serde::Serialize;
use deno_core::url;
use deno_core::ModuleSpecifier;
use std::collections::HashSet;
use std::fmt;
use std::hash::Hash;
#[cfg(not(test))]
use std::io;
use std::path::{Path, PathBuf};
#[cfg(test)]
use std::sync::atomic::AtomicBool;
#[cfg(test)]
use std::sync::atomic::Ordering;
#[cfg(test)]
use std::sync::Mutex;

const PERMISSION_EMOJI: &str = "⚠️";

/// Tri-state value for storing permission state
#[derive(PartialEq, Debug, Clone, Copy, Deserialize, PartialOrd)]
pub enum PermissionState {
    Granted = 0,
    Prompt = 1,
    Denied = 2,
}

impl PermissionState {
    /// Check the permission state.
    fn check(self, name: &str, info: Option<&str>) -> Result<(), AnyError> {
        if self == PermissionState::Granted {
            log_perm_access(&format!(
                "{} access{}",
                name,
                info.map_or(Default::default(), |info| { format!(" to {}", info) }),
            ));
            return Ok(());
        }
        let message = format!(
            "Requires {} access{}, run again with the --allow-{} flag",
            name,
            info.map_or(Default::default(), |info| { format!(" to {}", info) }),
            name
        );
        Err(custom_error("PermissionDenied", message))
    }
}

impl fmt::Display for PermissionState {
    fn fmt(&self, f: &mut fmt::Formatter<'_>) -> fmt::Result {
        match self {
            PermissionState::Granted => f.pad("granted"),
            PermissionState::Prompt => f.pad("prompt"),
            PermissionState::Denied => f.pad("denied"),
        }
    }
}

impl Default for PermissionState {
    fn default() -> Self {
        PermissionState::Prompt
    }
}

#[derive(Clone, Debug, Default, PartialEq)]
pub struct UnitPermission {
    pub name: &'static str,
    pub description: &'static str,
    pub state: PermissionState,
}

impl UnitPermission {
    pub fn query(&self) -> PermissionState {
        self.state
    }

    pub fn request(&mut self) -> PermissionState {
        if self.state == PermissionState::Prompt {
            if permission_prompt(&format!("access to {}", self.description)) {
                self.state = PermissionState::Granted;
            } else {
                self.state = PermissionState::Denied;
            }
        }
        self.state
    }

    pub fn revoke(&mut self) -> PermissionState {
        if self.state == PermissionState::Granted {
            self.state = PermissionState::Prompt;
        }
        self.state
    }

    pub fn check(&self) -> Result<(), AnyError> {
        self.state.check(self.name, None)
    }
}

#[derive(Clone, Debug, Default, Deserialize, PartialEq)]
pub struct UnaryPermission<T: Eq + Hash> {
    #[serde(skip)]
    pub name: &'static str,
    #[serde(skip)]
    pub description: &'static str,
    pub global_state: PermissionState,
    pub granted_list: HashSet<T>,
    pub denied_list: HashSet<T>,
}

#[derive(Clone, Eq, PartialEq, Hash, Debug, Default, Deserialize)]
pub struct ReadDescriptor(pub PathBuf);

#[derive(Clone, Eq, PartialEq, Hash, Debug, Default, Deserialize)]
pub struct WriteDescriptor(pub PathBuf);

#[derive(Clone, Eq, PartialEq, Hash, Debug, Default, Deserialize)]
pub struct NetDescriptor(pub String, pub Option<u16>);

impl NetDescriptor {


    pub fn from_string(host: String) -> Self {
        let url = url::Url::parse(&format!("http://{}", host)).unwrap();
        let hostname = url.host_str().unwrap().to_string();

        NetDescriptor(hostname, url.port())
    }
}

