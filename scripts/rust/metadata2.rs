use std::cell::RefCell;
use std::collections::HashSet;

fn main() {
    let mut tags_to_edit : Vec<_> = VEC_ITEMS.split("\n").collect();
    let mut tags_to_edit = RefCell::new(tags_to_edit);

//println!("len={}{:?}",tags_to_edit.len(),tags_to_edit);
    let mut found : Vec<_> = vec![];

    let mut replaced_count = 0u8;
    let output =TRACK_METADATA_STR.split("\n")
        .map(|s|{
            for tag in tags_to_edit.borrow().iter() {
                if s.contains(tag) {
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
// println!("{:?}",TRACK_METADATA_STR.find("RECORD_LABEL"));
// let hashset1 : HashSet<&&str>= HashSet::from_iter(&tags_to_edit);
//    let hashset2 : HashSet<&&str> = HashSet::from_iter(found.into_iter());
//  let difference: HashSet<&&&str> = hashset1.difference(&hashset2).collect();
// let difference: Vec<_> = found.iter().filter(|item| !set1.contains(**item)).cloned().collect();
//println!("diff={:?}\n",difference);
    println!("diff={:?}\n",tags_to_edit);
    println!("replaced_count = {replaced_count}\n{output}");
//  for value in found.into_iter().cloned() {
//        tags_to_edit.retain(|x| x != &value);
//}

//    println!("diff={:?}\n",tags_to_edit);

//println!("{}",TRACK_METADATA_STR.replace("{","lazy {"));
//println!("{}",TRACK_METADATA_STR.chars().filter(|c| *c == '{').count())
}