use std::cell::RefCell;
use std::collections::HashSet;

fn main() {
    let mut tags_to_edit : Vec<_> = VEC_ITEMS.split("\n").collect();
    let mut tags_to_edit = RefCell::new(tags_to_edit);

    println!("len={}{:?}",tags_to_edit.borrow().len(),tags_to_edit);
    let mut found : Vec<_> = vec![];

    let mut replaced_count = 0u8;
    let output =TRACK_METADATA_STR.split("\n")
        .map(|s|{
            for tag in tags_to_edit.borrow().iter() {
                if s.contains(&format!("FieldKey.{tag},")) {
                    replaced_count += 1;
                    found.push(tag.to_string());
//  tags_to_edit.retain(|x| x != tag);
                    return s.replace(")",",TrackMetadataInputType.TextWithNoRecommendation)");
                }
            }

            s.to_string()
        })
        .collect::<Vec<_>>()
        .join("\n");

    let tags_to_edit = tags_to_edit.borrow().iter().map(|s|s.to_string()).collect::<Vec<_>>().retain(|v| !found.iter().any(|a| &v==&a));
    println!("diff={:?}\n",tags_to_edit);
    println!("replaced_count = {replaced_count}\n{output}");
}