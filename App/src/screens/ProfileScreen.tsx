import {
  Text,
  View,
  TouchableOpacity,
  FlatList,
  RefreshControl,
  TouchableWithoutFeedback,
  } from "react-native";
import React, { useCallback, useEffect, useRef, useState } from "react";
import { useFocusEffect } from "@react-navigation/native";
import StreakFlame, { inkFor, tierFor } from "../components/StreakFlame";
import { getBestStreak } from "../repositories/UserRepository";
import Skeleton from "../components/Skeleton";
import EmptyState from "../components/EmptyState";
import { UserOut } from "../utils/User";
import { NativeStackScreenProps } from "@react-navigation/native-stack";
import { ProfileStackProps } from "../navigation/ProfileStackNavigation";
import Icon from "react-native-vector-icons/Ionicons";
import {
  getUser,
  getOwnUser,
  followUser,
  unfollowUser,
} from "../repositories/UserRepository";
import {
  bgColor,
  borderColor,
  convertQuantityToString,
  iconColor,
  raisedBgColor,
  textColor,
} from "../utils/Utils";
import Avatar from "../components/Avatar";
import { fonts } from "../styles/theme";
import ProfilePost from "../components/ProfilePost";
import FollowCount from "../components/FollowCount";
import Picture from "../components/Picture";
import ProfileNavigation from "../components/ProfileNavigation";
import { translations } from "../../translations/translation";
import { useSettingsContext } from "../contexts/SettingsContextProvider";
import Post from "../components/Post";
import { Post as PostDomain } from "../utils/Post";
import { getUserPosts } from "../repositories/PostRepository";
import { useTokenContext } from "../contexts/TokenContextProvider";
import { useImageContext } from "../contexts/ImageContextProvider";
import { Badge } from "../utils/Badge";
import { getBadgesByEmail } from "../repositories/BadgeRepository";
import BadgeInfo from "../components/BadgeInfo";
import { errorMessage } from "../utils/graphql";
import { useToast } from "../contexts/FeedbackContextProvider";

type Props = NativeStackScreenProps<ProfileStackProps, "Profile">;

const ProfileScreen = ({ navigation, route }: Props) => {
  const toast = useToast();
  const [user, setUser] = useState<UserOut>({} as UserOut);
  const [followers, setFollowers] = useState<string>("");
  const [following, setFollowing] = useState<string>("");
  const [load, setLoad] = useState<boolean>(false);
  const [isLoadingMore, setIsLoadingMore] = useState<boolean>(false);
  const [selectedPost, setSelectedPost] = useState<PostDomain>(null);
  const [selectedBadge, setSelectedBadge] = useState<Badge>(null);
  const { language, darkmode } = useSettingsContext();
  const { email } = useTokenContext();
  const [posts, setPosts] = useState<PostDomain[]>([]);
  const lastDate = useRef<string>(new Date().toISOString().slice(0, 19));
  const { uri } = useImageContext();
  const [badges, setBadges] = useState<Badge[]>([]);

  const [initialLoading, setInitialLoading] = useState<boolean>(true);
  const [bestStreak, setBestStreak] = useState<number>(0);
  const focusedOnce = useRef(false);

  // Each step receives the freshly loaded user instead of reading `user` from
  // state, which is still the previous value right after setUser().
  const init = async (append = false) => {
    const loaded = await fetchUser();
    if (!loaded?.email) {
      setInitialLoading(false);
      return;
    }
    await Promise.all([
      fetchPosts(loaded.email, append),
      append ? Promise.resolve() : fetchBadges(loaded.email),
      append ? Promise.resolve() : fetchBestStreak(loaded.email),
    ]);
    setInitialLoading(false);
  };

  useEffect(() => {
    setInitialLoading(true);
    lastDate.current = new Date().toISOString().slice(0, 19);
    init();
  }, [route.params?.email, uri]);

  useEffect(() => {
    if (load || isLoadingMore) {
      init(isLoadingMore);
    }
  }, [load, isLoadingMore]);

  // Coming back to the profile (e.g. after following someone) refreshes counts.
  useFocusEffect(
    useCallback(() => {
      if (!focusedOnce.current) {
        focusedOnce.current = true;
        return;
      }
      lastDate.current = new Date().toISOString().slice(0, 19);
      init();
    }, [route.params?.email])
  );

  const fetchUser = async (): Promise<UserOut | null> => {
    try {
      const loaded: UserOut = route.params?.email
        ? await getUser(route.params?.email)
        : await getOwnUser();
      setUser(loaded);
      setFollowers(convertQuantityToString(loaded.followers));
      setFollowing(convertQuantityToString(loaded.following));
      return loaded;
    } catch (error) {
      toast.error(errorMessage(error));
      return null;
    } finally {
      setLoad(false);
    }
  };

  const fetchPosts = async (userEmail: string, append: boolean) => {
    try {
      const response = await getUserPosts(lastDate.current, userEmail);
      setPosts((previous) => {
        if (!append) return response;
        const seen = new Set(previous.map((p) => p.id));
        return [...previous, ...response.filter((p) => !seen.has(p.id))];
      });
    } catch (error) {
      toast.error(errorMessage(error));
    } finally {
      setLoad(false);
      setIsLoadingMore(false);
    }
  };

  const fetchBadges = async (userEmail: string) => {
    try {
      setBadges(await getBadgesByEmail(userEmail));
    } catch (error) {
      console.error("Error fetching badges:", errorMessage(error));
    }
  };

  const fetchBestStreak = async (userEmail: string) => {
    try {
      setBestStreak(await getBestStreak(userEmail));
    } catch {
      setBestStreak(0);
    }
  };

  const reload = () => {
    lastDate.current = new Date().toISOString().slice(0, 19);
    setLoad(true);
  };

  const loadMore = () => {
    if (isLoadingMore || posts.length === 0) return;
    lastDate.current = posts[posts.length - 1].createdAt;
    setIsLoadingMore(true);
  };

  const handleFollow = async () => {
    try {
      if (user.isFollowing) {
        await unfollowUser(user.email);
      } else {
        await followUser(user.email);
      }
      setUser({
        ...user,
        isFollowing: !user.isFollowing,
        followers: user.isFollowing ? user.followers - 1 : user.followers + 1,
      });

      const updatedFollowers: number = user.isFollowing
        ? Math.max(0, user.followers - 1)
        : user.followers + 1;
      setFollowers(convertQuantityToString(updatedFollowers));
    } catch (error) {
      toast.error(errorMessage(error));
    }
  };

  return (
    <View className={`flex-1 ${bgColor(darkmode)}`}>
      {selectedPost && (
        <View className="absolute w-full h-full z-10">
          <TouchableWithoutFeedback onPress={() => setSelectedPost(null)}>
            <View className="flex-1 bg-black opacity-80 w-full h-full absolute"></View>
          </TouchableWithoutFeedback>
          <View className="absolute justify-center h-full">
            <Post post={selectedPost} navigation={navigation} />
          </View>
        </View>
      )}
      {selectedBadge && (
        <View className="absolute w-full h-full z-10">
          <TouchableWithoutFeedback onPress={() => setSelectedBadge(null)}>
            <View className="absolute w-full h-full bg-black opacity-80" />
          </TouchableWithoutFeedback>
          <View className="flex-1 justify-center items-center">
            <BadgeInfo item={selectedBadge} />
          </View>
        </View>
      )}

      {!!route.params?.email && (
        <ProfileNavigation navigation={navigation} message={user.username} />
      )}
      <View className={`px-5 pt-6 pb-5 border-b ${borderColor(darkmode)}`}>
        {!route.params?.email && (
          <TouchableOpacity
            onPress={() => navigation.navigate("Settings")}
            hitSlop={10}
            className="absolute top-6 right-5 z-10"
          >
            <Icon name="settings-outline" size={22} color={iconColor(darkmode)} />
          </TouchableOpacity>
        )}
        <View className="flex-row items-center">
          <TouchableOpacity
            onPress={() =>
              navigation.navigate("ProfilePictureScreen", { email: user.email })
            }
            disabled={!!route.params?.email}
            activeOpacity={0.8}
          >
            <Avatar image={user?.image} name={user?.username} size={80} />
          </TouchableOpacity>
          <View className="flex-1 ml-5">
            <View className="flex-row items-center pr-8">
              <Text
                numberOfLines={1}
                style={{ fontFamily: fonts.display, flexShrink: 1 }}
                className={`text-2xl ${textColor(darkmode)}`}
              >
                {user?.username}
              </Text>
              {bestStreak > 0 && (
                <View className="flex-row items-center ml-2">
                  <StreakFlame streak={bestStreak} size={16} glow={false} />
                  <Text
                    style={{
                      fontFamily: fonts.displayMedium,
                      color: inkFor(tierFor(bestStreak), darkmode),
                    }}
                    className="text-base ml-1"
                  >
                    {bestStreak}
                  </Text>
                </View>
              )}
            </View>
            <FollowCount
              followers={followers}
              following={following}
              email={user.email}
              navigation={navigation}
            />
          </View>
        </View>
        {user.email && user.email !== email && (
          <TouchableOpacity
            activeOpacity={0.85}
            className={`h-10 mt-5 rounded-xl items-center justify-center ${
              user.isFollowing
                ? `border ${borderColor(darkmode)} ${raisedBgColor(darkmode)}`
                : "bg-[#C2410C]"
            }`}
            onPress={handleFollow}
          >
            <Text
              className={`text-sm font-semibold ${
                user.isFollowing ? textColor(darkmode) : "text-white"
              }`}
            >
              {user.isFollowing
                ? translations[language || "en-EN"].screens.Profile.unfollow
                : translations[language || "en-EN"].screens.Profile.follow}
            </Text>
          </TouchableOpacity>
        )}
      </View>
      <FlatList
        style={{ width: "100%", flexGrow: 0 }}
        contentContainerStyle={{ paddingHorizontal: 12 }}
        className={badges.length ? `py-4 border-b ${borderColor(darkmode)}` : ""}
        horizontal
        data={badges}
        renderItem={({ item }) => {
          return (
            <TouchableOpacity onPress={() => setSelectedBadge(item)}>
              <Picture
                image={item.image}
                size={56}
                style="rounded-full mx-2"
              />
            </TouchableOpacity>
          );
        }}
      />

      <FlatList
        refreshControl={<RefreshControl refreshing={load} onRefresh={reload} />}
        style={{ width: "100%" }}
        contentContainerStyle={{ padding: 2 }}
        data={posts}
        renderItem={({ item }) => {
          return (
            <ProfilePost
              item={item}
              method={(item: PostDomain) => {
                setSelectedPost(item);
              }}
            />
          );
        }}
        keyExtractor={(item) => item.id}
        numColumns={3}
        ListEmptyComponent={
          initialLoading ? (
            <View className="flex-row flex-wrap">
              {[0, 1, 2].map((i) => (
                <Skeleton
                  key={i}
                  width="32.5%"
                  height={150}
                  radius={0}
                  style={{ margin: 2 }}
                />
              ))}
            </View>
          ) : (
            <EmptyState
              icon="images-outline"
              title={translations[language || "en-EN"].screens.Profile.noPosts}
              hint={translations[language || "en-EN"].screens.Profile.noPostsHint}
            />
          )
        }
        onEndReached={loadMore}
        onEndReachedThreshold={0.5}
      />
    </View>
  );
};

export default ProfileScreen;
